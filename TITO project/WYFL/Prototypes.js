String.prototype.toHex = function() {
    return Buffer(this.toString()).toString("hex");
};

String.prototype.fromHex = function () {
    return Buffer(this.toString(), 'hex').toString();
}