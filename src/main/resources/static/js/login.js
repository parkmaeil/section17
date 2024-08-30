function login(){
    var formData={
       username : document.getElementById("username").value,
       password : document.getElementById("password").value
    };
    fetch("/login", {
           method : "POST",
           headers : {
                  "Content-Type" : "application/json"
           },
           body : JSON.stringify(formData)
         })
    .then(response=>{
        if(!response.ok){
            throw new Error("Login failed");
        }
        // JWT 토큰을 받기
        var jwtToken=response.headers.get("Authorization");
        if(jwtToken && jwtToken.startsWith("Bearer ")){
             jwtToken=jwtToken.slice(7);
             localStorage.setItem("token", jwtToken);
             console.log("Received JWT token:"+jwtToken); // ?
             const authorities=jwtParsing(jwtToken);
             console.log("authorities:"+authorities); // [     ,    ]
             authorities.forEach(role=>{
                 switch(role){
                        case "ROLE_USER" :
                            document.getElementById("userMenu").style.display="block";
                            break;
                        case "ROLE_MANAGER" :
                            document.getElementById("managerMenu").style.display="block";
                            break;
                        case "ROLE_ADMIN" :
                           document.getElementById("adminMenu").style.display="block";
                           break;
                        default :
                           break;
                 }
               });  // forEach()
               // none login form | Display block
              document.getElementById("loginFom").style.display="none";
              document.getElementById("greeting").style.display="block";
              document.getElementById("usernameDisplay").innerText=formData.username;
              }else{
                      console.log("Invalid JWT token received");
             }
      })
    .catch(error=>{
       console.log("Login failed:", error);
    });
}
function jwtParsing(token){ // [0].[1],[2]
   try{
          const tokenPayload=token.split(".")[1];
          const decodedPayload=atob(tokenPayload);
          const payloadJSON=JSON.parse(decodedPayload);
          const authorities=payloadJSON.authorities;
          return authorities; // [   ]
      }catch(error){
          console.log("error");
          return [];
      }
}
function logout(){
       localStorage.removeItem("token");
        location.href="/";
}