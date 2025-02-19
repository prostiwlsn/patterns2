using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace patterns2_infoauth.Migrations
{
    /// <inheritdoc />
    public partial class init2 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "UserInfo");

            migrationBuilder.AddColumn<string>(
                name: "Name",
                table: "UserCredentials",
                type: "text",
                nullable: false,
                defaultValue: "");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Name",
                table: "UserCredentials");

            migrationBuilder.CreateTable(
                name: "UserInfo",
                columns: table => new
                {
                    UserCredentialsId = table.Column<Guid>(type: "uuid", nullable: false),
                    Name = table.Column<string>(type: "text", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_UserInfo", x => x.UserCredentialsId);
                    table.ForeignKey(
                        name: "FK_UserInfo_UserCredentials_UserCredentialsId",
                        column: x => x.UserCredentialsId,
                        principalTable: "UserCredentials",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });
        }
    }
}
