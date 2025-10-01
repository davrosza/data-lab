package common

case class ExampleDTO (
    id: String,
    firstName: String,
    lastName: String,
    projects: Seq[ProjectDTO]
)