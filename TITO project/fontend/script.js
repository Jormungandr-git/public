window.addEventListener('load', (event) => {
    
    let hash = document.getElementById("hashInput").value;

    document.getElementById("hashInput").addEventListener("change",function(){
        hash = document.getElementById("hashInput").value;
        console.log("CHANGGE")
        /// SEND A GET REQUEST TO SERVER TO GET THE VALUES OF THAT HASH

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                ///
            }
        };
        xhttp.open("POST","http://192.168.1.113:6969",true)
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send({
            type:"POST",
            body:{
                myHash: hash,
            },
            
            success: function (results) {
                alert("DONE!");
                console.log(results);            },
            error: function (result) {
                alert("FAILED!") 
                console.log(results);            },

            
        })

    })

    document.getElementById("authorize").addEventListener("click",function(){

        const countryFrom = document.getElementById("countryFrom").value;
        const countryTo = document.getElementById("countryTo").value;
        hash = document.getElementById("hashInput").value;
        /// SEND A POST REQUEST WITH countryFrom, countryTo and hash 

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                ///
            }
        };
        xhttp.open("POST","http://192.168.1.113:6969",true)
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhttp.send({
            "type":"POST",
            "body":{
                "myHash": hash,
                "from":countryFrom,
                "to":countryTo,
            }            
        })


    })









  });