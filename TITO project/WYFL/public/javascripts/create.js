window.addEventListener("load", function(){
    this.document.getElementById("submitCreate").addEventListener("click", function(){
        let firstname = document.getElementById("firstname").value;
        let lastname = document.getElementById("lastname").value;
        let ID = document.getElementById("ID").value;
        let countryTo = document.getElementById("countryTo").value;
        axios.post("/create", {
            "firstname": firstname,
            "lastname": lastname,
            "ID": ID,
            "countryTo": countryTo.toUpperCase()
        },
        {
            "headers":{
                "Content-Type":"application/json"
            }
        })
        .then((res)=>{
            console.log(res);
            document.getElementById("currentHash").value = res.data.newHash;
        })
        .then((err)=>{
            console.log(err);
        })
    })
});