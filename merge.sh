GH_TOKEN = "ba871564c3319a29384f8b897e3bfe3c5474492b"
if [ "$TRAVIS_BRANCH" != "test" ]; then     
	exit 0;
fi

git config -- global user.email= “julijanas.jezov@gmail.com”
git config -- global user.name = “julijanas jezov”

git checkout master || exit
git merge "$TRAVIS_COMMIT" || exit
git push origin master https://$GH_TOKEN@github.com/JulijanasJezov/im-client.git