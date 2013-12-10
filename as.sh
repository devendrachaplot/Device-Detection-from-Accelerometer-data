while read line; do
  	#echo $line
  	Array=($line)
	awk '{if($1=='${Array[0]}'){printf("1 ");} else{printf("0 ");} print $2,$3,$4,$5;}' traindata_with_t > train_models/traindata_${Array[0]}
	java svm_train -s 3 train_models/traindata_${Array[0]} train_models/traindata_${Array[0]}.model
done < traindata_with_t