## PERFORMANCE TEST FOR DSS

1. create a directory under dss with the name as testing date(like 20200802 => /benchmark/dss/20200802)
2. push your test code under src directory(like /benchmark/dss/20200802/src)
3. push the test report file contains following info
    - server spec(hardware and software)
    - test client spec(hardware and software)
    - test result
        - gatling report sample: https://gatling.io/docs/current/general/reports/
        - contains total request, success request, min/max/mean response time, req/s