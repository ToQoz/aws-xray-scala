package net.toqoz.aws.xray.jdbc;

import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Driver implements java.sql.Driver {
    private final static String PREFIX = "jdbc:xray:";
    private final static Integer MAJOR_VERSION = 0;
    private final static Integer MINOR_VERSION = 0;
    private final static Pattern PATTERN = Pattern.compile("(?<sep>[?&])driver=(?<driver>[^&]+)&?");
    private static ConcurrentHashMap<String, java.sql.Driver> backendDrivers = new ConcurrentHashMap<>();
    static java.sql.Driver getDriver(String driverName, String url) throws SQLException {
        if (driverName == null) return DriverManager.getDriver(url);

        java.sql.Driver backend = backendDrivers.get(driverName);
        if (backend != null) return backend;

        try {
            final Class<?> backendClass = Class.forName(driverName);
            backend = (java.sql.Driver) backendClass.newInstance();
            backendDrivers.put(driverName, backend);
            return backend;
        } catch (final ClassNotFoundException e) {
            final String msg = MessageFormat.format("Cloud not find {0} for {1}",  driverName, url);
            throw new RuntimeException(msg);
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new SQLException(e);
        }
    }
    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public java.sql.Connection connect(final String url, final Properties info) throws SQLException {
        // Attempts to make a database connection to the given URL.
        // The driver should return "null" if it realizes it is the wrong kind of driver to connect to the given URL.
        // This will be common, as when the JDBC driver manager is asked to connect to a given URL it passes the URL to each loaded driver in turn.
        // https://docs.oracle.com/javase/8/docs/api/java/sql/Driver.html
        if (!acceptsURL(url)) return null;
        final String newUrl = extractUrl(url);
        return new Connection(getDriver(extractDriverName(url), newUrl).connect(newUrl, info));
    }

    @Override
    public boolean acceptsURL(final String url) {
        return url != null && url.startsWith(PREFIX);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
        final String newUrl = extractUrl(url);
        return getDriver(extractDriverName(url), newUrl).getPropertyInfo(newUrl, info);
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    String extractDriverName(String url) {
        Matcher m = PATTERN.matcher(url);
        m.find();
        return m.group("driver");
    }

    String extractUrl(String url) {
        Matcher m = PATTERN.matcher(url);
        return m.replaceFirst("${sep}").replaceFirst(PREFIX, "jdbc:").replaceFirst("[?&]$", "");
    }
}
