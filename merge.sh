GH_TOKEN = 038a1052519222dc31b94323137ed6d562a5fa9d
if [ "$TRAVIS_BRANCH" != "test" ]; then     
	exit 0;
fi

git config -- global user.email= “julijanas.jezov@gmail.com”
git config -- global user.name = “julijanas jezov”

git checkout master || exit
git merge "$TRAVIS_COMMIT" || exit
git push origin master $GH_TOKEN@github.com/JulijanasJezov/im-client.git