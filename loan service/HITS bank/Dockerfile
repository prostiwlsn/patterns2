FROM mcr.microsoft.com/dotnet/aspnet:7.0 AS base
WORKDIR /app
EXPOSE 90
EXPOSE 543

FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build
WORKDIR /src
COPY . .
RUN dotnet restore "HITS bank.csproj"
RUN dotnet build "HITS bank.csproj" -c Release -o /app/build
#ARG BUILD_CONFIGURATION=Release
#WORKDIR /src
#COPY ["HITS bank/HITS bank.csproj", "HITS bank/"]
#RUN dotnet restore "HITS bank/HITS bank.csproj"
#COPY . .
#WORKDIR "/src/HITS bank"
#RUN dotnet build "HITS bank.csproj" -c $BUILD_CONFIGURATION -o /app/build

FROM build AS publish
RUN dotnet publish "HITS bank.csproj" -c Release -o /app/publish /p:UseAppHost=false

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "HITS bank.dll"]
