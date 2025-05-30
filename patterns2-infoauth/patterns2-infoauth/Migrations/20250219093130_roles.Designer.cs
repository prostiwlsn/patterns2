﻿// <auto-generated />
using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;
using patterns2_infoauth.Data;

#nullable disable

namespace patterns2_infoauth.Migrations
{
    [DbContext(typeof(AuthDbContext))]
    [Migration("20250219093130_roles")]
    partial class roles
    {
        /// <inheritdoc />
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "9.0.2")
                .HasAnnotation("Relational:MaxIdentifierLength", 63);

            NpgsqlModelBuilderExtensions.UseIdentityByDefaultColumns(modelBuilder);

            modelBuilder.Entity("patterns2_infoauth.Model.UserCredentials", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("uuid");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Password")
                        .IsRequired()
                        .HasColumnType("text");

                    b.Property<string>("Phone")
                        .IsRequired()
                        .HasColumnType("text");

                    b.HasKey("Id");

                    b.ToTable("UserCredentials");
                });

            modelBuilder.Entity("patterns2_infoauth.Model.UserRole", b =>
                {
                    b.Property<Guid>("UserCredentialsId")
                        .HasColumnType("uuid");

                    b.HasKey("UserCredentialsId");

                    b.ToTable("UserRole");
                });

            modelBuilder.Entity("patterns2_infoauth.Model.UserRole", b =>
                {
                    b.HasOne("patterns2_infoauth.Model.UserCredentials", "UserCredentials")
                        .WithMany()
                        .HasForeignKey("UserCredentialsId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("UserCredentials");
                });
#pragma warning restore 612, 618
        }
    }
}
