def staticStackName = "versapipelinetesting"

pipeline  {
   agent any
 stages       {
      stage('Read Test Inputs from CSV File') {
         steps {
            echo "Configuration"

            checkout([$class: 'GitSCM',
                branches: [[name: '*/master']],
                doGenerateSubmoduleConfigurations: false,
                extensions: [[$class: 'CleanCheckout']],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: '82f6da67-14bb-4091-84fb-05ec00d7a2d8', url: 'https://github.com/KaurBhavleen/testrepo.git']]
            ])
            script {
              def counter=0
              readFile("/var/lib/jenkins/workspace/Test_Pipeline2/pipeline-inputs.csv").split('\n').each { line, count ->
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
                 stackName = staticStackName + c
                 


                     results = ansibleTower async: false, credential: '', extraVars: """---
  stackName:  "${stackName}"
  regionName:  "${awsRegion}"
  

  """, importTowerLogs: true, importWorkflowChildLogs: false, inventory: '', jobTags: '', jobTemplate: 'AwsBranchDeletion', jobType: 'run', limit: '', removeColor: false, skipJobTags: '', templateType: 'job', throwExceptionWhenFail: true, towerCredentialsId: 'AnsibleTowerAuth', towerServer: 'AnsibleTowerAWS', verbose: false

                }

      }
        }
          }




    }
}
