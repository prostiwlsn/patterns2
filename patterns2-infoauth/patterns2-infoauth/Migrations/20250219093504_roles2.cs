using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace patterns2_infoauth.Migrations
{
    /// <inheritdoc />
    public partial class roles2 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_UserRole",
                table: "UserRole");

            migrationBuilder.AddPrimaryKey(
                name: "PK_UserRole",
                table: "UserRole",
                columns: new[] { "Role", "UserCredentialsId" });

            migrationBuilder.CreateIndex(
                name: "IX_UserRole_UserCredentialsId",
                table: "UserRole",
                column: "UserCredentialsId");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_UserRole",
                table: "UserRole");

            migrationBuilder.DropIndex(
                name: "IX_UserRole_UserCredentialsId",
                table: "UserRole");

            migrationBuilder.AddPrimaryKey(
                name: "PK_UserRole",
                table: "UserRole",
                column: "UserCredentialsId");
        }
    }
}
