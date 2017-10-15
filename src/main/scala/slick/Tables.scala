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
  lazy val schema
    : profile.SchemaDescription = DevelopersTable.schema ++ SmilesExtractionsTable.schema ++ SmilesGenerationTable.schema ++ SmilesTable.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table DevelopersTable
    *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
    *  @param username Database column username SqlType(VARCHAR), Length(255,true)
    *  @param email Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
  case class DevelopersRow(id: Int, username: String, email: Option[String] = None)

  /** GetResult implicit for fetching DevelopersRow objects using plain SQL queries */
  implicit def GetResultDevelopersRow(implicit e0: GR[Int],
                                      e1: GR[String],
                                      e2: GR[Option[String]]): GR[DevelopersRow] = GR { prs =>
    import prs._
    DevelopersRow.tupled((<<[Int], <<[String], <<?[String]))
  }

  /** Table description of table developers. Objects of this class serve as prototypes for rows in queries. */
  class DevelopersTable(_tableTag: Tag) extends Table[DevelopersRow](_tableTag, "developers") {
    def * = (id, username, email) <> (DevelopersRow.tupled, DevelopersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(id), Rep.Some(username), email).shaped.<>({ r =>
        import r._; _1.map(_ => DevelopersRow.tupled((_1.get, _2.get, _3)))
      }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    /** Database column username SqlType(VARCHAR), Length(255,true) */
    val username: Rep[String] = column[String]("username", O.Length(255, varying = true))

    /** Database column email SqlType(VARCHAR), Length(255,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(255, varying = true), O.Default(None))
  }

  /** Collection-like TableQuery object for table DevelopersTable */
  lazy val DevelopersTable = new TableQuery(tag => new DevelopersTable(tag))

  /** Entity class storing rows of table SmilesExtractionsTable
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param extractionDate Database column extraction_date SqlType(TIMESTAMP)
    *  @param numberOfSmilesExtracted Database column number_of_smiles_extracted SqlType(INT) */
  case class SmilesExtractionsRow(id: Long, extractionDate: java.sql.Timestamp, numberOfSmilesExtracted: Int)

  /** GetResult implicit for fetching SmilesExtractionsRow objects using plain SQL queries */
  implicit def GetResultSmilesExtractionsRow(implicit e0: GR[Long],
                                             e1: GR[java.sql.Timestamp],
                                             e2: GR[Int]): GR[SmilesExtractionsRow] = GR { prs =>
    import prs._
    SmilesExtractionsRow.tupled((<<[Long], <<[java.sql.Timestamp], <<[Int]))
  }

  /** Table description of table smiles_extractions. Objects of this class serve as prototypes for rows in queries. */
  class SmilesExtractionsTable(_tableTag: Tag) extends Table[SmilesExtractionsRow](_tableTag, "smiles_extractions") {
    def * =
      (id, extractionDate, numberOfSmilesExtracted) <> (SmilesExtractionsRow.tupled, SmilesExtractionsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(id), Rep.Some(extractionDate), Rep.Some(numberOfSmilesExtracted)).shaped.<>(
        { r =>
          import r._; _1.map(_ => SmilesExtractionsRow.tupled((_1.get, _2.get, _3.get)))
        },
        (_: Any) => throw new Exception("Inserting into ? projection not supported.")
      )

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column extraction_date SqlType(TIMESTAMP) */
    val extractionDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("extraction_date")

    /** Database column number_of_smiles_extracted SqlType(INT) */
    val numberOfSmilesExtracted: Rep[Int] = column[Int]("number_of_smiles_extracted")
  }

  /** Collection-like TableQuery object for table SmilesExtractionsTable */
  lazy val SmilesExtractionsTable = new TableQuery(tag => new SmilesExtractionsTable(tag))

  /** Entity class storing rows of table SmilesGenerationTable
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param generationDate Database column generation_date SqlType(TIMESTAMP)
    *  @param smileId Database column smile_id SqlType(BIGINT), Default(None)
    *  @param error Database column error SqlType(VARCHAR), Length(255,true), Default(None) */
  case class SmilesGenerationRow(id: Long,
                                 generationDate: java.sql.Timestamp,
                                 smileId: Option[Long] = None,
                                 error: Option[String] = None)

  /** GetResult implicit for fetching SmilesGenerationRow objects using plain SQL queries */
  implicit def GetResultSmilesGenerationRow(implicit e0: GR[Long],
                                            e1: GR[java.sql.Timestamp],
                                            e2: GR[Option[Long]],
                                            e3: GR[Option[String]]): GR[SmilesGenerationRow] = GR { prs =>
    import prs._
    SmilesGenerationRow.tupled((<<[Long], <<[java.sql.Timestamp], <<?[Long], <<?[String]))
  }

  /** Table description of table smiles_generation. Objects of this class serve as prototypes for rows in queries. */
  class SmilesGenerationTable(_tableTag: Tag) extends Table[SmilesGenerationRow](_tableTag, "smiles_generation") {
    def * = (id, generationDate, smileId, error) <> (SmilesGenerationRow.tupled, SmilesGenerationRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(id), Rep.Some(generationDate), smileId, error).shaped.<>({ r =>
        import r._; _1.map(_ => SmilesGenerationRow.tupled((_1.get, _2.get, _3, _4)))
      }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column generation_date SqlType(TIMESTAMP) */
    val generationDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("generation_date")

    /** Database column smile_id SqlType(BIGINT), Default(None) */
    val smileId: Rep[Option[Long]] = column[Option[Long]]("smile_id", O.Default(None))

    /** Database column error SqlType(VARCHAR), Length(255,true), Default(None) */
    val error: Rep[Option[String]] = column[Option[String]]("error", O.Length(255, varying = true), O.Default(None))

    /** Foreign key referencing SmilesTable (database name smiles_generation_ibfk_1) */
    lazy val smilesTableFk = foreignKey("smiles_generation_ibfk_1", smileId, SmilesTable)(
      r => Rep.Some(r.id),
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction)
  }

  /** Collection-like TableQuery object for table SmilesGenerationTable */
  lazy val SmilesGenerationTable = new TableQuery(tag => new SmilesGenerationTable(tag))

  /** Entity class storing rows of table SmilesTable
    *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
    *  @param creationDate Database column creation_date SqlType(TIMESTAMP)
    *  @param photoUrl Database column photo_url SqlType(VARCHAR), Length(2083,true), Default(None)
    *  @param description Database column description SqlType(VARCHAR), Length(280,true), Default(None)
    *  @param source Database column source SqlType(VARCHAR), Length(255,true)
    *  @param sourceUrl Database column source_url SqlType(VARCHAR), Length(2083,true)
    *  @param numberOfLikes Database column number_of_likes SqlType(BIGINT)
    *  @param sent Database column sent SqlType(BIT)
    *  @param sentDate Database column sent_date SqlType(DATETIME), Default(None)
    *  @param smileNumber Database column smile_number SqlType(INT), Default(None) */
  case class SmilesRow(id: Long,
                       creationDate: java.sql.Timestamp,
                       photoUrl: Option[String] = None,
                       description: Option[String] = None,
                       source: String,
                       sourceUrl: String,
                       numberOfLikes: Long,
                       sent: Boolean,
                       sentDate: Option[java.sql.Timestamp] = None,
                       smileNumber: Option[Int] = None)

  /** GetResult implicit for fetching SmilesRow objects using plain SQL queries */
  implicit def GetResultSmilesRow(implicit e0: GR[Long],
                                  e1: GR[java.sql.Timestamp],
                                  e2: GR[Option[String]],
                                  e3: GR[String],
                                  e4: GR[Boolean],
                                  e5: GR[Option[java.sql.Timestamp]],
                                  e6: GR[Option[Int]]): GR[SmilesRow] = GR { prs =>
    import prs._
    SmilesRow.tupled(
      (<<[Long],
       <<[java.sql.Timestamp],
       <<?[String],
       <<?[String],
       <<[String],
       <<[String],
       <<[Long],
       <<[Boolean],
       <<?[java.sql.Timestamp],
       <<?[Int]))
  }

  /** Table description of table smiles. Objects of this class serve as prototypes for rows in queries. */
  class SmilesTable(_tableTag: Tag) extends Table[SmilesRow](_tableTag, "smiles") {
    def * =
      (id, creationDate, photoUrl, description, source, sourceUrl, numberOfLikes, sent, sentDate, smileNumber) <> (SmilesRow.tupled, SmilesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      (Rep.Some(id),
       Rep.Some(creationDate),
       photoUrl,
       description,
       Rep.Some(source),
       Rep.Some(sourceUrl),
       Rep.Some(numberOfLikes),
       Rep.Some(sent),
       sentDate,
       smileNumber).shaped.<>(
        { r =>
          import r._; _1.map(_ => SmilesRow.tupled((_1.get, _2.get, _3, _4, _5.get, _6.get, _7.get, _8.get, _9, _10)))
        },
        (_: Any) => throw new Exception("Inserting into ? projection not supported.")
      )

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)

    /** Database column creation_date SqlType(TIMESTAMP) */
    val creationDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("creation_date")

    /** Database column photo_url SqlType(VARCHAR), Length(2083,true), Default(None) */
    val photoUrl: Rep[Option[String]] =
      column[Option[String]]("photo_url", O.Length(2083, varying = true), O.Default(None))

    /** Database column description SqlType(VARCHAR), Length(280,true), Default(None) */
    val description: Rep[Option[String]] =
      column[Option[String]]("description", O.Length(280, varying = true), O.Default(None))

    /** Database column source SqlType(VARCHAR), Length(255,true) */
    val source: Rep[String] = column[String]("source", O.Length(255, varying = true))

    /** Database column source_url SqlType(VARCHAR), Length(2083,true) */
    val sourceUrl: Rep[String] = column[String]("source_url", O.Length(2083, varying = true))

    /** Database column number_of_likes SqlType(BIGINT) */
    val numberOfLikes: Rep[Long] = column[Long]("number_of_likes")

    /** Database column sent SqlType(BIT) */
    val sent: Rep[Boolean] = column[Boolean]("sent")

    /** Database column sent_date SqlType(DATETIME), Default(None) */
    val sentDate: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("sent_date", O.Default(None))

    /** Database column smile_number SqlType(INT), Default(None) */
    val smileNumber: Rep[Option[Int]] = column[Option[Int]]("smile_number", O.Default(None))
  }

  /** Collection-like TableQuery object for table SmilesTable */
  lazy val SmilesTable = new TableQuery(tag => new SmilesTable(tag))
}
