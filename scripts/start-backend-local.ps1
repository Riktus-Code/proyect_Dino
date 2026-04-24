$ErrorActionPreference = 'Stop'

$projectRoot = Split-Path -Parent $PSScriptRoot
$javaHome = 'C:\Program Files\Java\jdk-17'

if (!(Test-Path "$javaHome\bin\java.exe")) {
    throw "No se encontro JDK 17 en $javaHome. Instala JDK 17 o actualiza la ruta en scripts/start-backend-local.ps1"
}

$env:JAVA_HOME = $javaHome
$env:Path = "$javaHome\bin;$env:Path"

$toolsDir = Join-Path $projectRoot '.tools'
$mavenVersion = '3.9.9'
$mavenDirName = "apache-maven-$mavenVersion"
$cachedMavenHome = Join-Path $toolsDir $mavenDirName
$cachedMavenCmd = Join-Path $cachedMavenHome 'bin\\mvn.cmd'

$mavenCmd = $cachedMavenCmd

if (!(Test-Path $mavenCmd)) {
    New-Item -ItemType Directory -Path $toolsDir -Force | Out-Null

    $downloadUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/$mavenDirName-bin.zip"
    $tempZip = Join-Path $env:TEMP ("$mavenDirName-" + [guid]::NewGuid().ToString('N') + '.zip')
    $tempExtract = Join-Path $env:TEMP ("$mavenDirName-" + [guid]::NewGuid().ToString('N'))

    Write-Host "Descargando Maven $mavenVersion..."
    Invoke-WebRequest -Uri $downloadUrl -OutFile $tempZip

    Write-Host "Extrayendo Maven..."
    Expand-Archive -Path $tempZip -DestinationPath $tempExtract -Force

    $extractedMavenHome = Join-Path $tempExtract $mavenDirName
    if (!(Test-Path $extractedMavenHome)) {
        $fallback = Get-ChildItem -Path $tempExtract -Directory | Select-Object -First 1
        if ($null -eq $fallback) {
            throw 'No se pudo extraer Maven correctamente.'
        }
        $extractedMavenHome = $fallback.FullName
    }

    $mavenCmd = Join-Path $extractedMavenHome 'bin\\mvn.cmd'
    if (!(Test-Path $mavenCmd)) {
        throw "No se encontro mvn.cmd en $extractedMavenHome"
    }
}

$env:Path = "$(Split-Path -Parent $mavenCmd);$env:Path"

Set-Location (Join-Path $projectRoot 'apps\wedding-api')
Write-Host "Iniciando backend en local (sin Docker)..."
& $mavenCmd spring-boot:run
