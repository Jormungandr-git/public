String.prototype.toHex = function() {
    return Buffer(this.toString()).toString("hex");
};

String.prototype.fromHex = function() {
    return Buffer(this.toString(), 'hex').toString();
}

String.prototype.cleanData = function() {
    const a = this.toString();
    for(i = 0; i < a.length; i++) {
        if(a[i] == '{') return a.slice(i);
    }
}

String.prototype.sendLog = function() {
    return console.log(`[${new Date().toLocaleString("en-GB").replace(",", "")}]${this.toString()}`);
};