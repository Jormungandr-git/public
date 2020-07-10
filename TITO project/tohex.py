def str_to_hex(a):
    tmp = bytes(a,"utf-8")
    return "".join([f"{hex(x)[2:]:0>2}" for x in tmp])

inp = input()
print(str_to_hex(inp))