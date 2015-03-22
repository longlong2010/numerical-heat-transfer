for x in `find . -name "*.java"`
do
	t1=`stat -f %m $x`
	c=`echo $x | sed 's/\.java$/\.class/'`
	t2=`stat -f %m $c`
	if [ $t1 -gt $t2 ]
	then
		echo "javac $x"
		javac "$x"
	fi
done
