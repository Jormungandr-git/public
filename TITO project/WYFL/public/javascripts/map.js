function moveIndicator(indicator, c){
    if(c==null) return;
    //console.log(c);
    indicator.style.visibility = "visible"; 
    let rectC = c.getBoundingClientRect();
    let rectI = indicator.getBoundingClientRect();
    let canvas = document.getElementById("canvas");
    let ctx = canvas.getContext("2d");
    ctx.lineTo(rectC.x+rectC.width/2, rectC.y+rectC.height/2 + 28);
    ctx.lineWidth = 2;
    ctx.stroke();
    indicator.style.left = Math.floor(rectC.x+rectC.width/2-10)+"px";
    let offset = document.getElementById("header").getBoundingClientRect().height;
    indicator.style.top = Math.floor(rectC.y+rectC.height/2+offset-20)+"px";
    c.style.fill = "rgb(238, 255, 0)";
}
function animateTravels(indicator, data, index, iframe){
    if(index == data.length) return;
    moveIndicator(indicator, iframe.querySelector("path[id="+data[index][1]+"]"))
    setTimeout(()=>animateTravels(indicator, data, index+1, iframe),2500);
}
window.addEventListener("load", function(){
    let indicator = document.getElementById("indicator");
    let iframe_doc = document.getElementById("svgframe").contentWindow.document;
    let svg = iframe_doc.querySelector("svg");
    svg.style.width = "100%";
    svg.style.height = "100%";
    let canvas = document.getElementById("canvas");
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    let countries = iframe_doc.getElementsByTagName("path");
    //this.console.log(countries);
    for(let c of countries){
        c.style.stroke = "white";
        c.style.fill = "teal";
        c.style.transitionDelay = "2s";
    }
    document.getElementById("submithash").addEventListener("click", ()=>{
        axios.post("/path/", {
            "currentHash": document.getElementById("currentHash").value
        }, {
            headers: { "Content-Type": "application/json" }
        })
        .then((res)=>{
            console.log(res);
            animateTravels(indicator, res.data, 0, iframe_doc);
        })
        .then((err)=>{
            console.log(err);
        });
    });
});