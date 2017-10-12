package slick
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = DevelopersTable.schema ++ SmilesExtractionsTable.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table DevelopersTable
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(VARCHAR), Length(255,true)
   *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
  case class DevelopersRow(id: Int, username: String, email: Option[String] = None)
  /** GetResult implicit for fetching DevelopersRow objects using plain SQL queries */
  implicit def GetResultDevelopersRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[DevelopersRow] = GR{
    prs => import prs._
    DevelopersRow.tupled((<<[Int], <<[String], <<?[String]))
  }
  /** Table description of table developers. Objects of this class serve as prototypes for rows in queries. */
  class DevelopersTable(_tableTag: Tag) extends Table[DevelopersRow](_tableTag, "developers") {
    def * = (id, username, email) <> (DevelopersRow.tupled, DevelopersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(username), email).shaped.<>({r=>import r._; _1.map(_=> DevelopersRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username SqlType(VARCHAR), Length(255,true) */
    val username: Rep[String] = column[String]("username", O.Length(255,varying=true))
    /** Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(255,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table DevelopersTable */
  lazy val DevelopersTable = new TableQuery(tag => new DevelopersTable(tag))

  /** Entity class storing rows of table SmilesExtractionsTable
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param extractionDate Database column extraction_date SqlType(TIMESTAMP)
   *  @param numberOfSmilesExtracted Database column number_of_smiles_extracted SqlType(INT), Default(None) */
  case class SmilesExtractionsRow(id: Long, extractionDate: java.sql.Timestamp, numberOfSmilesExtracted: Option[Int] = None)
  /** GetResult implicit for fetching SmilesExtractionsRow objects using plain SQL queries */
  implicit def GetResultSmilesExtractionsRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp], e2: GR[Option[Int]]): GR[SmilesExtractionsRow] = GR{
    prs => import prs._
    SmilesExtractionsRow.tupled((<<[Long], <<[java.sql.Timestamp], <<?[Int]))
  }
  /** Table description of table smiles_extractions. Objects of this class serve as prototypes for rows in queries. */
  class SmilesExtractionsTable(_tableTag: Tag) extends Table[SmilesExtractionsRow](_tableTag, "smiles_extractions") {
    def * = (id, extractionDate, numberOfSmilesExtracted) <> (SmilesExtractionsRow.tupled, SmilesExtractionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(extractionDate), numberOfSmilesExtracted).shaped.<>({r=>import r._; _1.map(_=> SmilesExtractionsRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column extraction_date SqlType(TIMESTAMP) */
    val extractionDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("extraction_date")
    /** Database column number_of_smiles_extracted SqlType(INT), Default(None) */
    val numberOfSmilesExtracted: Rep[Option[Int]] = column[Option[Int]]("number_of_smiles_extracted", O.Default(None))
  }
  /** Collection-like TableQuery object for table SmilesExtractionsTable */
  lazy val SmilesExtractionsTable = new TableQuery(tag => new SmilesExtractionsTable(tag))
}
