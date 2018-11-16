package app.models

import scalikejdbc._

case class User(id: Long, name: String)

object User extends SQLSyntaxSupport[User] {
  override val tableName = "users"
  override val columnNames = Seq("id", "name")

  def apply(u: ResultName[User])(rs: WrappedResultSet) = new User(rs.long(u.id), rs.string(u.name))

  private val u = User.syntax("u")

  def findAll()(implicit session: DBSession): Seq[User] = {
    withSQL { select.from(User as u) }.map(User(u.resultName)).list.apply()
  }

  def find(id: Long)(implicit session: DBSession): Option[User] = {
    withSQL { select.from(User as u).where.eq(u.id, id) }.map(User(u.resultName)).single.apply()
  }

  def create(name: String)(implicit session: DBSession): Long = {
    withSQL { insert.into(User).namedValues(User.column.name -> name) }.updateAndReturnGeneratedKey.apply()
  }
}
