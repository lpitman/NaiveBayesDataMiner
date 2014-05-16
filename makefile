JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        NaiveBayesClassifier.java \
        AVList.java \
        DataBase.java \
				Target.java \
				ValueClassPair.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
