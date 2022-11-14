#./bin/bash
# 定义颜色
BLUE_COLOR="\033[36m"
RED_COLOR="\033[31m"
GREEN_COLOR="\033[32m"
VIOLET_COLOR="\033[35m"
RES="\033[0m"

echo -e "${BLUE_COLOR}# ######################################################################${RES}"
echo -e "${BLUE_COLOR}#                       Docker ELK UnDeploy Script                     #${RES}"
echo -e "${BLUE_COLOR}# ######################################################################${RES}"

# 部署项目
echo -e "${BLUE_COLOR}==================> Docker UnDeploy Start <==================${RES}"
docker-compose stop
docker-compose rm
