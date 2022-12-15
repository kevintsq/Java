import sympy
import xeger
import subprocess
import time
import re
import os
regex = r'([+-])?([+-])?(([+-]?[1-9]\d{0,10})|(x(\*\*[+-]?[1-9]\d{0,10})?))(\*(([+-]?[1-9]\d{0,10})|(x(\*\*[+-]?[1-9]\d{0,10})?))){0,20}([+-]([+-])?(([+-]?[1-9]\d{0,10})|(x(\*\*[+-]?[1-9]\d{0,20})?))(\*(([+-]?[1-9]\d{0,20})|(x(\*\*[+-]?[1-9]\d{0,20})?))){0,20}){0,20}'
for i in range(40, 400):
    print("----------TEST" + str(i))
    s = xeger.Xeger(limit=100).xeger(regex)
    x = sympy.Symbol('x')
    # f = eval(s)
    if os.path.exists("name"+str(i+1)+".in"):
        filename = "name"+str(i+1)+".in"
    else:
        filename = "name"+str(i+1)+".in.txt"
    with open(filename) as file:
        print(filename)
        rawf = f = file.read()
        with open('in', 'w') as ffile:
            ffile.write(f)
            ffile.close()
        f = f.replace(" ", "").replace("\t", "")
    f =eval(re.sub(r'\b0+(?!\b)', '', f))
    f_ = sympy.diff(f, x)
    in_file = open('in', 'r')
    out_file = open('out', 'w')
    print(f'TestPoint{i} : {rawf}')
    p = subprocess.Popen(f'java -jar Unit1.jar', stdin=in_file, stdout=out_file, shell=True)
    time.sleep(0.05)
    while p.poll() is None:
        pass
    in_file.close()
    out_file.close()
    with open('out', 'r') as file:
        global out
        out = file.readline()
    if (out == ""): continue
    F = f_ - eval(out)
    boo = bool(F == 0)
    if boo:
        print(f'AC\n'
              f'SYMP ~ {str(f_)}\n'
              f'YOUR ~ {out}')
    else:
        print(f'WA\n'
              f'SYMP ~ {str(f_)}\n'
              f'YOUR ~ {out}'
              f'Delta: {F}')
        break
        # with open('ans', 'w') as file:
        #     file.write(str(f_))
        #     file.write("\n\n\n")
        #     file.close()