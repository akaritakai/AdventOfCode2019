
FROM gradle:6.0.1-jdk13

WORKDIR "/opt/aoc"

ADD build.gradle ./build.gradle
ADD cookie.txt ./cookie.txt
Add puzzle ./puzzle
ADD src ./src

ENTRYPOINT ["gradle", "run"]
