using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace HITS_bank.Migrations
{
    public partial class AddAccountId : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "CurrencyEnum",
                table: "Loans",
                newName: "Currency");

            migrationBuilder.AddColumn<Guid>(
                name: "AccountId",
                table: "Loans",
                type: "uuid",
                nullable: false,
                defaultValue: new Guid("00000000-0000-0000-0000-000000000000"));
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "AccountId",
                table: "Loans");

            migrationBuilder.RenameColumn(
                name: "Currency",
                table: "Loans",
                newName: "CurrencyEnum");
        }
    }
}
