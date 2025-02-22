FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS base
WORKDIR /app
EXPOSE 8080
EXPOSE 8081

FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src
# Copy everything together if the project is in the root:
COPY . .
RUN dotnet restore "patterns2-infoauth.csproj"
RUN dotnet build "patterns2-infoauth.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "patterns2-infoauth.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "patterns2-infoauth.dll"]