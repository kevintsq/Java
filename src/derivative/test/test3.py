import re
import subprocess
import sys
from multiprocessing import *

import sympy


def test(path, i, j, original_expr, original_str, master_expr, master_str):
    with open("results.txt", "ab") as f:
        try:
            query_result = subprocess.run(f"java -jar {path}",
                                          input=original_expr.value.decode("ascii"), stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                          encoding="ascii", timeout=10)
        except subprocess.TimeoutExpired:
            print(f"QUERY {j}: Time limit exceeded", file=sys.stderr)
            f.write(original_str.value)
            f.write(master_str.value)
            f.write(b"\n\n")
            return

        if query_result.stderr != "":
            print(f"QUERY {j}: Runtime Error\n{query_result.stderr}", file=sys.stderr)
            f.write(original_str.value)
            f.write(master_str.value)
            f.write(b"\n\n")
        else:
            print(f"QUERY {j}: {query_result.stdout}")
            if master_expr.equals(query_result.stdout):
                print(f"Test case {i} for QUERY {j} passed.\n\n")
            else:
                print(f"QUERY {j}'s output on test case {i} is wrong.\n\n", file=sys.stderr)
                f.write(original_str.value)
                f.write(master_str.value)
                f.write(b"\n\n")


if __name__ == '__main__':
    paths = (
        r"C:\Users\tanta\Downloads\04ee679b8da50775347f8e4f602bb4b2561855cf\out\artifacts\04ee679b8da50775347f8e4f602bb4b2561855cf_jar\04ee679b8da50775347f8e4f602bb4b2561855cf.jar",
        r"C:\Users\tanta\Downloads\05ca5b9896b244da741aca7b110d3be5497a1729\out\artifacts\05ca5b9896b244da741aca7b110d3be5497a1729_jar\05ca5b9896b244da741aca7b110d3be5497a1729.jar",
        r"C:\Users\tanta\Downloads\063b026650a6938becd977f5c11a2e110d74a97b\out\artifacts\063b026650a6938becd977f5c11a2e110d74a97b_jar\063b026650a6938becd977f5c11a2e110d74a97b.jar",
        r"C:\Users\tanta\Downloads\b983ca2e9d2d55c82bea2dba7f3580c01e02a471\out\artifacts\b983ca2e9d2d55c82bea2dba7f3580c01e02a471_jar\b983ca2e9d2d55c82bea2dba7f3580c01e02a471.jar",
        r"C:\Users\tanta\Downloads\da823ad165e6c276a49f6fd4fa5d8f1c844125e5\out\artifacts\da823ad165e6c276a49f6fd4fa5d8f1c844125e5_jar\da823ad165e6c276a49f6fd4fa5d8f1c844125e5.jar",
        r"C:\Users\tanta\Downloads\eae1a085fc4bc8e6607a99de10466004ee4b9a1c\out\artifacts\eae1a085fc4bc8e6607a99de10466004ee4b9a1c_jar\eae1a085fc4bc8e6607a99de10466004ee4b9a1c.jar")

    x = sympy.symbols("x")
    regex = re.compile(r"\b0+(\d+)")

    for i in range(19, 301):
        with open(f"testcase/name{i}.in") as g:
            original_expr = g.read().replace("\t", "").replace(" ", "")
        for match in regex.finditer(original_expr):
            original_expr = regex.sub(lambda matched: matched.group(1), original_expr)
        original_str = f"ORIGINAL: {original_expr}"
        print(original_str)
        master_expr = sympy.diff(original_expr, x)
        master_str = f"MASTER: {master_expr}"
        print(master_str)
        original_expr_array = Array("c", original_expr.encode('ascii'))
        original_str_array = Array("c", original_str.encode('ascii'))
        master_str_array = Array("c", master_str.encode('ascii'))
        processes = [Process(target=test, args=(paths[j], i, j, original_expr_array, original_str_array,
                                                master_expr, master_str_array))
                     for j in range(len(paths))]
        for process in processes:
            process.start()
        for process in processes:
            process.join()
