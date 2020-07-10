window.addEventListener('load', (event) => {
    
    let hash = document.getElementById("hashInput").value;

    document.getElementById("hashInput").addEventListener("change", function(){
        const currentHash = document.getElementById("hashInput").value;
        /// SEND A GET REQUEST TO SERVER TO GET THE VALUES OF THAT HASH
        axios.post("/getUser/", {
            "currentHash": currentHash
        }, {
            headers: { "Content-Type": "application/json" }
        })
        .then((res)=>{
            const user = JSON.parse(res.data);
            console.log("User: " + user);
            if(user) {
                document.getElementById("name").value = user.name;
                document.getElementById("lastName").value = user.lastName;
                document.getElementById("id").value = user.id;
                document.getElementById("nationality").value = user.nationality;
                document.getElementById("status").value = user.status;
            }
        })
        .then((err)=>{
            console.log(err);
        });
        

    })

    document.getElementById("authorize").addEventListener("click",function(){

        const countryFrom = document.getElementById("countryFrom").value;
        const countryTo = document.getElementById("countryTo").value;
        const currentHash = document.getElementById("hashInput").value;
        /// SEND A POST REQUEST WITH countryFrom, countryTo and hash 

        axios.post("/travel/", {
            "countryFrom": countryFrom.toUpperCase(),
            "countryTo": countryTo.toUpperCase(),
            "currentHash": currentHash
        }, {
            headers: { "Content-Type": "application/json" }
        })
        .then((res)=>{
            let tmp = res.data.newHash;
            console.log("New hash " + tmp);
            if(tmp) document.getElementById("hashInput").value = tmp;
        })
        .then((err)=>{
            console.log(err);
        });
    })
  });