import sympy
from random import randint
import subprocess
import os


NEG_HUGE_NUM = -31415926535897932384626433
HUGE_NUM = 31415926535897932384626433

x = sympy.symbols("x")
start_symbols = ("", "+", "-")
symbols = ("+", "-", "*")


def gen_factor(option):
    if option == 0:
        return str(randint(NEG_HUGE_NUM, HUGE_NUM))
    elif option == 1:
        return "x**" + str(randint(NEG_HUGE_NUM, HUGE_NUM))
    elif option == 2:
        return "x"
    else:
        return str(randint(-1, 1))


cnt = 0
while 10:
    cnt += 1
    original_expr = start_symbols[randint(0, 2)]
    for _ in range(20):
        original_expr += gen_factor(randint(0, 5)) + symbols[randint(0, 2)]
    original_expr += gen_factor(randint(0, 5))

    print(f"ORIGINAL: {original_expr}")
    master_expr = sympy.diff(original_expr, x)
    print(f"MASTER: {master_expr}")
    query_expr = subprocess.run(
        r"java -jar C:\Users\tanta\Documents\Personal\Studies\Undergraduate\Java\out\artifacts\Java_jar\Java.jar",
        input=original_expr, stdout=subprocess.PIPE, encoding='utf-8').stdout
    print(f"QUERY: {query_expr}")
    if master_expr.equals(query_expr):
        print(f"Test case {cnt} passed.\n\n")
    else:
        print(f"Your output on test case {cnt} is wrong.\n\n")
        os.system("pause")
