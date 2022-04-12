<#import "/spring.ftl" as spring />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Set your password</h2>

    <@spring.bind "userRequestModel"/>
    <form action="/verify" method="post">
        Password:<br>
        <@spring.formInput "userRequestModel.password"/>
        <@spring.showErrors "<br>"/>
        <br><br>
        <input type="submit" value="Submit">
    </form>

</body>
</html>