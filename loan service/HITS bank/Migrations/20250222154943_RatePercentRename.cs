using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace HITS_bank.Migrations
{
    public partial class RatePercentRename : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "Rate",
                table: "Tariffs",
                newName: "RatePercent");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "RatePercent",
                table: "Tariffs",
                newName: "Rate");
        }
    }
}
