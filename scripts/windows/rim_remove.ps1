# Capture the current working directory and the directory where the script resides
$currentDir = (Get-Location).Path
$scriptDir = $PSScriptRoot

# Verify the script is being called from the directory it is stored in
if ($currentDir -ne $scriptDir) {
    Write-Host "Deletion canceled: This script must be run from the directory to be deleted." -ForegroundColor Red
    Write-Host "Please run the following command first, then try again:" -ForegroundColor Yellow
    Write-Host "cd `"$scriptDir`"" -ForegroundColor Cyan
    exit
}

# Define the desktop shortcut path
$desktopPath = [Environment]::GetFolderPath("Desktop")
$shortcutPath = Join-Path -Path $desktopPath -ChildPath "rim-tool.lnk"

# Display targets and prompt the user for confirmation
Write-Host "WARNING: This action permanently deletes files without using the Recycle Bin." -ForegroundColor Yellow
Write-Host "Directory to delete : $currentDir"
Write-Host "Shortcut to delete  : $shortcutPath"

$confirmation = Read-Host "Are you sure you want to proceed? (Y/N)"

if ($confirmation -match '^[Yy]$') {
    # Step out of the current directory so Windows does not lock it during deletion
    Set-Location -Path (Split-Path -Path $currentDir -Parent)

    # Delete the directory and all its contents
    if (Test-Path -LiteralPath $currentDir) {
        Remove-Item -LiteralPath $currentDir -Recurse -Force
        Write-Host "Deleted directory: $currentDir" -ForegroundColor Green
    }

    # Delete the desktop shortcut if it exists
    if (Test-Path -LiteralPath $shortcutPath) {
        Remove-Item -LiteralPath $shortcutPath -Force
        Write-Host "Deleted shortcut: $shortcutPath" -ForegroundColor Green
    } else {
        Write-Host "Shortcut not found: $shortcutPath" -ForegroundColor DarkGray
    }
} else {
    Write-Host "Deletion canceled." -ForegroundColor Cyan
}
