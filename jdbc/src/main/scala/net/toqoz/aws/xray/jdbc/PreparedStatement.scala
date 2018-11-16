package net.toqoz.aws.xray.jdbc

import java.io.{ InputStream, Reader }
import java.math.BigDecimal
import java.net.URL
import java.sql.{
  PreparedStatement => IPreparedStatement,
  ParameterMetaData,
  ResultSet,
  ResultSetMetaData,
  RowId,
  SQLXML,
  Ref,
  Clob,
  Blob,
  NClob,
  Date,
  Time,
  Timestamp
}
import java.util.{ Map => JavaMap, Calendar }

class PreparedStatement(stmt: IPreparedStatement, subsegmentName: String, sql: String, additionalParams: JavaMap[String, AnyRef])
    extends Statement(stmt, subsegmentName, additionalParams)
    with IPreparedStatement {
  override def executeQuery: ResultSet = tracing(sql, () => stmt.executeQuery())
  override def executeUpdate: Int = tracing(sql, () => stmt.executeUpdate())
  override def setNull(index: Int, sqlType: Int): Unit = stmt.setNull(index, sqlType)
  override def setBoolean(index: Int, x: Boolean): Unit = stmt.setBoolean(index, x)
  override def setByte(index: Int, x: Byte): Unit = stmt.setByte(index, x)
  override def setShort(index: Int, x: Short): Unit = stmt.setShort(index, x)
  override def setInt(index: Int, x: Int): Unit = stmt.setInt(index, x)
  override def setLong(index: Int, x: Long): Unit = stmt.setLong(index, x)
  override def setFloat(index: Int, x: Float): Unit = stmt.setFloat(index, x)
  override def setDouble(index: Int, x: Double): Unit = stmt.setDouble(index, x)
  override def setBigDecimal(index: Int, x: BigDecimal): Unit = stmt.setBigDecimal(index, x)
  override def setString(index: Int, x: String): Unit = stmt.setString(index, x)
  override def setBytes(index: Int, x: Array[Byte]): Unit = stmt.setBytes(index, x)
  override def setDate(index: Int, x: Date): Unit = stmt.setDate(index, x)
  override def setTime(index: Int, x: Time): Unit = stmt.setTime(index, x)
  override def setTimestamp(index: Int, x: Timestamp): Unit = stmt.setTimestamp(index, x)
  override def setAsciiStream(index: Int, x: InputStream, length: Int): Unit = stmt.setAsciiStream(index, x)
  @deprecated("", "")
  override def setUnicodeStream(index: Int, x: InputStream, length: Int): Unit = stmt.setUnicodeStream(index, x, length)
  override def setBinaryStream(index: Int, x: InputStream, length: Int): Unit = stmt.setBinaryStream(index, x, length)
  override def clearParameters(): Unit = stmt.clearParameters()
  // Advanced features
  override def setObject(index: Int, x: Any, targetSqlType: Int): Unit = stmt.setObject(index, x, targetSqlType)
  override def setObject(index: Int, x: Any): Unit = stmt.setObject(index, x)
  override def execute: Boolean = tracing(sql, () => stmt.execute())
  // JDBC 2.0
  override def addBatch(): Unit = stmt.addBatch()
  override def setCharacterStream(index: Int, reader: Reader, length: Int): Unit = stmt.setCharacterStream(index, reader, length)
  override def setRef(index: Int, x: Ref): Unit = stmt.setRef(index, x)
  override def setBlob(index: Int, x: Blob): Unit = stmt.setBlob(index, x)
  override def setClob(index: Int, x: Clob): Unit = stmt.setClob(index, x)
  override def setArray(index: Int, x: java.sql.Array): Unit = stmt.setArray(index, x)
  override def getMetaData: ResultSetMetaData = stmt.getMetaData
  override def setDate(index: Int, x: Date, cal: Calendar): Unit = stmt.setDate(index, x, cal)
  override def setTime(index: Int, x: Time, cal: Calendar): Unit = stmt.setTime(index, x, cal)
  override def setTimestamp(index: Int, x: Timestamp, cal: Calendar): Unit = stmt.setTimestamp(index, x, cal)
  override def setNull(index: Int, sqlType: Int, typeName: String): Unit = stmt.setNull(index, sqlType, typeName)
  // JDBC 3.0
  override def setURL(index: Int, x: URL): Unit = stmt.setURL(index, x)
  override def getParameterMetaData: ParameterMetaData = stmt.getParameterMetaData
  // JDBC 4.0
  override def setRowId(index: Int, x: RowId): Unit = stmt.setRowId(index, x)
  override def setNString(index: Int, value: String): Unit = stmt.setNString(index, value)
  override def setNCharacterStream(index: Int, value: Reader, length: Long): Unit = stmt.setNCharacterStream(index, value, length)
  override def setNClob(index: Int, value: NClob): Unit = stmt.setNClob(index, value)
  override def setClob(index: Int, reader: Reader, length: Long): Unit = stmt.setClob(index, reader, length)
  override def setBlob(index: Int, inputStream: InputStream, length: Long): Unit = stmt.setBlob(index, inputStream, length)
  override def setNClob(index: Int, reader: Reader, length: Long): Unit = stmt.setNClob(index, reader, length)
  override def setSQLXML(index: Int, xmlObject: SQLXML): Unit = stmt.setSQLXML(index, xmlObject)
  override def setObject(index: Int, x: Any, targetSqlType: Int, scaleOrLength: Int): Unit = stmt.setObject(index, targetSqlType, scaleOrLength)
  override def setAsciiStream(index: Int, x: InputStream, length: Long): Unit = stmt.setAsciiStream(index, x, length)
  override def setBinaryStream(index: Int, x: InputStream, length: Long): Unit = stmt.setBinaryStream(index, x, length)
  override def setCharacterStream(index: Int, reader: Reader, length: Long): Unit = stmt.setCharacterStream(index, reader, length)
  override def setAsciiStream(index: Int, x: InputStream): Unit = stmt.setAsciiStream(index, x)
  override def setBinaryStream(index: Int, x: InputStream): Unit = stmt.setBinaryStream(index, x)
  override def setCharacterStream(index: Int, reader: Reader): Unit = stmt.setCharacterStream(index, reader)
  override def setNCharacterStream(index: Int, value: Reader): Unit = stmt.setNCharacterStream(index, value)
  override def setClob(index: Int, reader: Reader): Unit = stmt.setClob(index, reader)
  override def setBlob(index: Int, inputStream: InputStream): Unit = stmt.setBlob(index, inputStream)
  override def setNClob(index: Int, reader: Reader): Unit = stmt.setNClob(index, reader)
}
