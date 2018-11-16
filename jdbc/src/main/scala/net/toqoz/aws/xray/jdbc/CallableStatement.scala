package net.toqoz.aws.xray.jdbc

import java.io.{ InputStream, Reader }
import java.math.BigDecimal
import java.net.URL
import java.sql.{ CallableStatement => ICallableStatement, Array => JavaArray, Blob, Clob, Date, NClob, Ref, RowId, SQLXML, Time, Timestamp }
import java.util.{ Calendar, Map => JavaMap }

class CallableStatement(stmt: ICallableStatement, subsegmentName: String, sql: String, additionalParams: JavaMap[String, AnyRef])
    extends PreparedStatement(stmt, subsegmentName, sql, additionalParams)
    with ICallableStatement {
  override def registerOutParameter(index: Int, sqlType: Int): Unit = stmt.registerOutParameter(index, sqlType)
  override def registerOutParameter(index: Int, sqlType: Int, scale: Int): Unit = stmt.registerOutParameter(index, sqlType, scale)
  override def wasNull: Boolean = stmt.wasNull()
  override def getString(index: Int): String = stmt.getString(index)
  override def getBoolean(index: Int): Boolean = stmt.getBoolean(index)
  override def getByte(index: Int): Byte = stmt.getByte(index)
  override def getShort(index: Int): Short = stmt.getShort(index)
  override def getInt(index: Int): Int = stmt.getInt(index)
  override def getLong(index: Int): Long = stmt.getLong(index)
  override def getFloat(index: Int): Float = stmt.getFloat(index)
  override def getDouble(index: Int): Double = stmt.getDouble(index)
  override def getBigDecimal(index: Int, scale: Int): BigDecimal = stmt.getBigDecimal(index)
  override def getBytes(index: Int): Array[Byte] = stmt.getBytes(index)
  override def getDate(index: Int): Date = stmt.getDate(index)
  override def getTime(index: Int): Time = stmt.getTime(index)
  override def getTimestamp(index: Int): Timestamp = stmt.getTimestamp(index)
  // Advanced features:
  override def getObject(index: Int): Object = stmt.getObject(index)
  // JDBC 2.0
  override def getBigDecimal(index: Int): BigDecimal = stmt.getBigDecimal(index)
  override def getObject(index: Int, map: JavaMap[String, Class[_]]): Object = stmt.getObject(index)
  override def getRef(index: Int): Ref = stmt.getRef(index)
  override def getBlob(index: Int): Blob = stmt.getBlob(index)
  override def getClob(index: Int): Clob = stmt.getClob(index)
  override def getArray(index: Int): JavaArray = stmt.getArray(index)
  override def getDate(index: Int, cal: Calendar): Date = stmt.getDate(index, cal)
  override def getTime(index: Int, cal: Calendar): Time = stmt.getTime(index, cal)
  override def getTimestamp(index: Int, cal: Calendar): Timestamp = stmt.getTimestamp(index, cal)
  override def registerOutParameter(index: Int, sqlType: Int, typeName: String): Unit = stmt.registerOutParameter(index, sqlType, typeName)
  // JDBC 3.0
  override def registerOutParameter(name: String, sqlType: Int): Unit = stmt.registerOutParameter(name, sqlType)
  override def registerOutParameter(name: String, sqlType: Int, scale: Int): Unit = stmt.registerOutParameter(name, sqlType, scale)
  override def registerOutParameter(name: String, sqlType: Int, typeName: String): Unit = stmt.registerOutParameter(name, sqlType, typeName)
  override def getURL(index: Int): URL = stmt.getURL(index)
  override def setURL(name: String, `val`: URL): Unit = stmt.setURL(name, `val`)
  override def setNull(name: String, sqlType: Int): Unit = stmt.setNull(name, sqlType)
  override def setBoolean(name: String, x: Boolean): Unit = stmt.setBoolean(name, x)
  override def setByte(name: String, x: Byte): Unit = stmt.setByte(name, x)
  override def setShort(name: String, x: Short): Unit = stmt.setShort(name, x)
  override def setInt(name: String, x: Int): Unit = stmt.setInt(name, x)
  override def setLong(name: String, x: Long): Unit = stmt.setLong(name, x)
  override def setFloat(name: String, x: Float): Unit = stmt.setFloat(name, x)
  override def setDouble(name: String, x: Double): Unit = stmt.setDouble(name, x)
  override def setBigDecimal(name: String, x: BigDecimal): Unit = stmt.setBigDecimal(name, x)
  override def setString(name: String, x: String): Unit = stmt.setString(name, x)
  override def setBytes(name: String, x: Array[Byte]): Unit = stmt.setBytes(name, x)
  override def setDate(name: String, x: Date): Unit = stmt.setDate(name, x)
  override def setTime(name: String, x: Time): Unit = stmt.setTime(name, x)
  override def setTimestamp(name: String, x: Timestamp): Unit = stmt.setTimestamp(name, x)
  override def setAsciiStream(name: String, x: InputStream, length: Int): Unit = stmt.setAsciiStream(name, x, length)
  override def setBinaryStream(name: String, x: InputStream, length: Int): Unit = stmt.setBinaryStream(name, x, length)
  override def setObject(name: String, x: Any, targetSqlType: Int, scale: Int): Unit = stmt.setObject(name, x, targetSqlType, scale)
  override def setObject(name: String, x: Any, targetSqlType: Int): Unit = stmt.setObject(name, x, targetSqlType)
  override def setObject(name: String, x: Any): Unit = stmt.setObject(name, x)
  override def setCharacterStream(name: String, reader: Reader, length: Int): Unit = stmt.setCharacterStream(name, reader, length)
  override def setDate(name: String, x: Date, cal: Calendar): Unit = stmt.setDate(name, x, cal)
  override def setTime(name: String, x: Time, cal: Calendar): Unit = stmt.setTime(name, x, cal)
  override def setTimestamp(name: String, x: Timestamp, cal: Calendar): Unit = stmt.setTimestamp(name, x, cal)
  override def setNull(name: String, sqlType: Int, typeName: String): Unit = stmt.setNull(name, sqlType, typeName)
  override def getString(name: String): String = stmt.getString(name)
  override def getBoolean(name: String): Boolean = stmt.getBoolean(name)
  override def getByte(name: String): Byte = stmt.getByte(name)
  override def getShort(name: String): Short = stmt.getShort(name)
  override def getInt(name: String): Int = stmt.getInt(name)
  override def getLong(name: String): Long = stmt.getLong(name)
  override def getFloat(name: String): Float = stmt.getFloat(name)
  override def getDouble(name: String): Double = stmt.getDouble(name)
  override def getBytes(name: String): Array[Byte] = stmt.getBytes(name)
  override def getDate(name: String): Date = stmt.getDate(name)
  override def getTime(name: String): Time = stmt.getTime(name)
  override def getTimestamp(name: String): Timestamp = stmt.getTimestamp(name)
  override def getObject(name: String): Object = stmt.getObject(name)
  override def getBigDecimal(name: String): BigDecimal = stmt.getBigDecimal(name)
  override def getObject(name: String, map: JavaMap[String, Class[_]]): Object = stmt.getObject(name)
  override def getRef(name: String): Ref = stmt.getRef(name)
  override def getBlob(name: String): Blob = stmt.getBlob(name)
  override def getClob(name: String): Clob = stmt.getClob(name)
  override def getArray(name: String): JavaArray = stmt.getArray(name)
  override def getDate(name: String, cal: Calendar): Date = stmt.getDate(name)
  override def getTime(name: String, cal: Calendar): Time = stmt.getTime(name)
  override def getTimestamp(name: String, cal: Calendar): Timestamp = stmt.getTimestamp(name)
  override def getURL(name: String): URL = stmt.getURL(name)
  // JDBC 4.0
  override def getRowId(index: Int): RowId = stmt.getRowId(index)
  override def getRowId(name: String): RowId = stmt.getRowId(name)
  override def setRowId(name: String, x: RowId): Unit = stmt.setRowId(name, x)
  override def setNString(name: String, value: String): Unit = stmt.setNString(name, value)
  override def setNCharacterStream(name: String, value: Reader, length: Long): Unit = stmt.setNCharacterStream(name, value, length)
  override def setNClob(name: String, value: NClob): Unit = stmt.setNClob(name, value)
  override def setClob(name: String, reader: Reader, length: Long): Unit = stmt.setClob(name, reader, length)
  override def setBlob(name: String, inputStream: InputStream, length: Long): Unit = stmt.setBlob(name, inputStream, length)
  override def setNClob(name: String, reader: Reader, length: Long): Unit = stmt.setNClob(name, reader, length)
  override def getNClob(index: Int): NClob = stmt.getNClob(index)
  override def getNClob(name: String): NClob = stmt.getNClob(name)
  override def setSQLXML(name: String, xmlObject: SQLXML): Unit = stmt.setSQLXML(name, xmlObject)
  override def getSQLXML(index: Int): SQLXML = stmt.getSQLXML(index)
  override def getSQLXML(name: String): SQLXML = stmt.getSQLXML(name)
  override def getNString(index: Int): String = stmt.getNString(index)
  override def getNString(name: String): String = stmt.getNString(name)
  override def getNCharacterStream(index: Int): Reader = stmt.getNCharacterStream(index)
  override def getNCharacterStream(name: String): Reader = stmt.getNCharacterStream(name)
  override def getCharacterStream(index: Int): Reader = stmt.getCharacterStream(index)
  override def getCharacterStream(name: String): Reader = stmt.getCharacterStream(name)
  override def setBlob(name: String, x: Blob): Unit = stmt.setBlob(name, x)
  override def setClob(name: String, x: Clob): Unit = stmt.setClob(name, x)
  override def setAsciiStream(name: String, x: InputStream, length: Long): Unit = stmt.setAsciiStream(name, x, length)
  override def setBinaryStream(name: String, x: InputStream, length: Long): Unit = stmt.setBinaryStream(name, x, length)
  override def setCharacterStream(name: String, reader: Reader, length: Long): Unit = stmt.setCharacterStream(name, reader, length)
  override def setAsciiStream(name: String, x: InputStream): Unit = stmt.setAsciiStream(name, x)
  override def setBinaryStream(name: String, x: InputStream): Unit = stmt.setBinaryStream(name, x)
  override def setCharacterStream(name: String, reader: Reader): Unit = stmt.setCharacterStream(name, reader)
  override def setNCharacterStream(name: String, value: Reader): Unit = stmt.setNCharacterStream(name, value)
  override def setClob(name: String, reader: Reader): Unit = stmt.setClob(name, reader)
  override def setBlob(name: String, inputStream: InputStream): Unit = stmt.setBlob(name, inputStream)
  override def setNClob(name: String, reader: Reader): Unit = stmt.setNClob(name, reader)
  // JDBC 4.1
  override def getObject[T](index: Int, `type`: Class[T]): T = stmt.getObject(index, `type`)
  override def getObject[T](name: String, `type`: Class[T]): T = stmt.getObject(name, `type`)
}
