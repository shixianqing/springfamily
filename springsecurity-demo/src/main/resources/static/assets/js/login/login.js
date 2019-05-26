function login() {
    var username = $("#login-box>div>div.widget-main>form>fieldset>label:nth-child(1)>span>input").val();
    var password = $("#login-box>div>div.widget-main>form>fieldset>label:nth-child(2)>span>input").val();

    console.log(username + "------" + password);
    
   $.ajax(
       {
           type : "POST",
           url : "/login",
           data : JSON.stringify({
               "username" : username,
               "password" : password
           }),
           success : function(data){
               console.log(data)
               if (data.code && data.code == 200){
                   window.location.href = "index.html"
               }
           },
           contentType : "application/json"

       }
   );

}