for branch in $(git branch -r | grep -v '\->'); do
    git branch --track "${branch#origin/}" "$branch" 2>/dev/null
done

git fetch --all

for branch in $(git for-each-ref --format '%(refname:short)' refs/heads/); do
    git checkout $branch
    git pull --rebase
done

