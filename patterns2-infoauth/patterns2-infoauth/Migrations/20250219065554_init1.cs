using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace patterns2_infoauth.Migrations
{
    /// <inheritdoc />
    public partial class init1 : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserInfo_UserCredentials_UserCredentialsId1",
                table: "UserInfo");

            migrationBuilder.DropIndex(
                name: "IX_UserInfo_UserCredentialsId1",
                table: "UserInfo");

            migrationBuilder.DropColumn(
                name: "UserCredentialsId1",
                table: "UserInfo");

            migrationBuilder.AddForeignKey(
                name: "FK_UserInfo_UserCredentials_UserCredentialsId",
                table: "UserInfo",
                column: "UserCredentialsId",
                principalTable: "UserCredentials",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_UserInfo_UserCredentials_UserCredentialsId",
                table: "UserInfo");

            migrationBuilder.AddColumn<Guid>(
                name: "UserCredentialsId1",
                table: "UserInfo",
                type: "uuid",
                nullable: false,
                defaultValue: new Guid("00000000-0000-0000-0000-000000000000"));

            migrationBuilder.CreateIndex(
                name: "IX_UserInfo_UserCredentialsId1",
                table: "UserInfo",
                column: "UserCredentialsId1");

            migrationBuilder.AddForeignKey(
                name: "FK_UserInfo_UserCredentials_UserCredentialsId1",
                table: "UserInfo",
                column: "UserCredentialsId1",
                principalTable: "UserCredentials",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
