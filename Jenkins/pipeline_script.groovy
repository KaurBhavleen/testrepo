
pipeline  {
   agent any
 stages       {
      stage('Read Test Inputs from CSV File') {
         steps {
            echo "Configuration"
        
            script {
              def counter=0
              readFile("Jenkins/pipeline-inputs.csv").split('\n').each { line, count ->
                def fields = line.split(',')
                counter = counter + 1
                for(String item: fields) {
                  echo line
                  echo count
                }
                if ( counter == 2 ){
                  echo "YES"
                  vpcCidrList = fields[3]
                  echo vpcCidrList
                  branchVPC = vpcCidrList.tokenize( '#' )
                  sdwanControllerIp = fields[1]
                  awsRegion = fields[2]
                }


              }
            }
          }
        }

stage('Terminate Cloudformation Stack') {
      steps {
      script {
      for (int c = 1; c <= branchVPC.size(); c++) {
          echo 'Test Chaining of Pipeline.'

                }

      }
        }
          }




    }
}
