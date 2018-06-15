gcloud compute scp build/libs/checklistsAPI-0.0.1-SNAPSHOT.jar $1:api.jar
gcloud compute ssh $1 --command 'sudo cp api.jar /var/spring'
gcloud compute ssh $1 --command 'sudo systemctl restart spring'
