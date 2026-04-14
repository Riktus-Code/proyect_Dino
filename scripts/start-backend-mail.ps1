$projectRoot = Split-Path -Parent $PSScriptRoot
$envFile = Join-Path $projectRoot '.env.local'

if (!(Test-Path $envFile)) {
    throw 'No se encontro .env.local en la raiz del proyecto.'
}

Get-Content $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line -and -not $line.StartsWith('#')) {
        $match = [regex]::Match($line, '^(?<key>[^=]+)=(?<value>.*)$')
        if ($match.Success) {
            $key = $match.Groups['key'].Value.Trim()
            $value = $match.Groups['value'].Value
            [System.Environment]::SetEnvironmentVariable($key, $value, 'Process')
        }
    }
}

$javaHome = 'C:\Users\elman\.jdks\openjdk-25'
$env:JAVA_HOME = $javaHome
$env:Path = "$javaHome\bin;D:\Apache maven\apache-maven-3.9.14\bin;$env:Path"

Set-Location $projectRoot
npm run start:back:jdk25
