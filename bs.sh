# gcc -o stringkernelTEST stringkerneltest.c suffix_tree.c
# gcc -o stringkernelTRAIN stringkerneltrain.c suffix_tree.c
# ./stringkernelTRAIN >traindata
# ./stringkernelTEST >testdata

rm answer.csv
touch answer.csv

for line in $(cat questions.csv);
do
	set -- $line 
	IFS=","; declare -a Array=($*)  
	awk '{if($1=='${Array[1]}'){print $2,$3,$4,$5;}}' testdata_with_t > testdata.new
	java svm_predict testdata.new train_models/traindata_${Array[2]}.model out
	echo "${Array[0]},$(cat out)" >> answer.csv
done