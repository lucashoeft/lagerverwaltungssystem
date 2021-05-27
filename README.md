# lagerverwaltungssystem

This warehouse management software was developed as a group project for the course 'Software Engineering 1' at Universität Potsdam.

## Features

* View, search and sort all products

<p align="center">
<img width="891" alt="Screenshot 2021-05-27 at 16 10 26" src="https://user-images.githubusercontent.com/26332559/119841758-6202ed00-bf06-11eb-9d41-40ca07cbfb0f.png">
</p>

* Create, update and delete products

<p align="center">
<img width="414" alt="Screenshot 2021-05-27 at 16 10 46" src="https://user-images.githubusercontent.com/26332559/119841800-6b8c5500-bf06-11eb-89e1-9720240814bc.png">
  
<img width="414" alt="Screenshot 2021-05-27 at 16 10 57" src="https://user-images.githubusercontent.com/26332559/119842065-a8f0e280-bf06-11eb-85d5-a73d3a9efebf.png">
</p>

* Create and delete categories

<p align="center">
<img width="570" alt="Screenshot 2021-05-27 at 16 10 54" src="https://user-images.githubusercontent.com/26332559/119842038-a098a780-bf06-11eb-8ae0-8dc53b194ed7.png">
</p>

* Handling wrong input by showing proper error message

<p align="center">
<img width="422" alt="Screenshot 2021-05-27 at 16 20 21" src="https://user-images.githubusercontent.com/26332559/119843105-857a6780-bf07-11eb-8256-b1c2ff74ccfb.png">
</p>

## Technology

* IDE: IntelliJ
* Java Version: JDK 1.8 / SE 8

## How to work on this repository (first steps)

Never commit directly into the master-branch!

Easy way: Use GitHub Desktop App

1. Clone the remote repository to your computer

2. Create a local branch with naming style `YOURNAME/develop` (e.g. lucas/develop )

3. Make changes

4. Commit changes and push it to the remote branch

5. Create a pull request on the website

Hard way: Terminal / Command Line

1. Go to the folder you want to create the local repository in

2. Create local repository based on remote repository

`$ git clone https://github.com/lucashoeft/lagerverwaltungssystem.git`

3. create local branch with naming style `YOURNAME/develop` (e.g. lucas/develop )

`$ git checkout -b YOURNAME/develop`

4. Checkout local branch

`$ git checkout BRANCH`

5. Create remote branch

`$ git push -u origin BRANCH`

6. Work on files

7. Add files to staging

`$ git add FILENAME`

See file status with `$ git status`

8. Commit files to local branch

`$ git commit -m 'message'`

9. Push changes from local branch to remote branch

`$ git push -u origin BRANCH`

10. Create pull request in GitHub if you think that you are done with a feature

## Links

* [Work with Branches](https://www.freecodecamp.org/forum/t/push-a-new-local-branch-to-a-remote-git-repository-and-track-it-too/13222)
