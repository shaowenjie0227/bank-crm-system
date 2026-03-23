$headers = @{
    "Authorization" = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiLnrqHnkIblkZgiLCJpYXQiOjE3NjA2MjU2ODUsImV4cCI6MTc2MDcxMjA4NX0.4hlR9NgYM_ZcNzLcP4EFbJQhVwvhR2oBwRYRa0bf8r5JrfFmIuzdlbUymu2_dkMQlkUqUe1YPPXjOkIh_xf3oA"
}

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8085/api/developments/list?page=1&size=10" -Method Get -Headers $headers
    Write-Host "成功响应: $($response | ConvertTo-Json -Depth 10)"
} catch {
    Write-Host "错误: $($_.Exception.Message)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $reader.DiscardBufferedData()
        $responseBody = $reader.ReadToEnd()
        Write-Host "错误响应: $responseBody"
    }
}