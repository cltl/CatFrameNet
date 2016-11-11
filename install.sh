set -e

#Requires an installation of maven 2.x and Java 1.6 or higher

# define the location of the install scipt
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PARENT="$( cd $DIR && cd .. && pwd)"

mkdir "$DIR/lib"
echo "#1. compiling the library from source code and dependencies"
mvn clean
mvn install
echo "#2. moving binary to lib folder"
echo "#3. installing the vua-resources"
cd "$PARENT"
git clone https://github.com/cltl/vua-resources.git
echo "#4. cleaning up"
cd "$DIR"
rm EventCoreference.iml
rm EventCoreference.iws
rm EventCoreference.ipr
