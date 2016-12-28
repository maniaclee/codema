var a = {match: /.*DTO/i.test($fromClassName), result: $fromClassName.replace(/DTO/i, 'Entity')}
(function () {
    return /.*DTO/i.test($fromClassName) ? $fromClassName.replace(/dto/i, 'Entity') : null
})()