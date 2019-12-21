FROM gradle:6.0.1-jdk13

WORKDIR "/opt/aoc"

COPY . .

ENTRYPOINT ["gradle", "run"]
