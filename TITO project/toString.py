def str_to_hex(a):
    tmp = bytes(a,"utf-8")
    return "".join([f"{hex(x)[2:]:0>2}" for x in tmp])
def hex_to_string(a):
    return "".join([chr(int(a[x:x+2],16)) for x in range(0,len(a),2)])
inp = input()
print("hex:",str_to_hex(inp))
print("str:",hex_to_string(inp))
