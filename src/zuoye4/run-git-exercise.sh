#!/bin/bash
# ============================================================
# Git 练习一键脚本（macOS / bash）
# 对应：java79-teach/教程文档/git/练习/git练习.md
# 远程平台：GitHub（不是码云）
#
# 用法（在另一台 Mac 上）：
#   1. 把本脚本拷过去
#   2. chmod +x run-git-exercise.sh
#   3. ./run-git-exercise.sh
#
# 安全提醒：跑完后请立刻到 GitHub 撤销本次 PAT！
# ============================================================

set -euo pipefail

# ---------- 可配置项 ----------
# 令牌只在本次运行中使用，不会写入 git remote / 提交记录
GITHUB_TOKEN="${GITHUB_TOKEN:-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}"
GITHUB_USER="${GITHUB_USER:-2318600486-coder}"
REPO_NAME="${REPO_NAME:-gitdir2022}"
# 练习根目录：默认放在「脚本同级目录」下的 gitdir
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
WORKDIR="${WORKDIR:-$BASE_DIR/gitdir}"
REMOTE_CLEAN="https://github.com/${GITHUB_USER}/${REPO_NAME}.git"
# ------------------------------

step() {
  echo
  echo "========== 步骤 $* =========="
}

need_cmd() {
  command -v "$1" >/dev/null 2>&1 || {
    echo "缺少命令: $1"
    exit 1
  }
}

need_cmd git
need_cmd curl
need_cmd python3

# 确保本机能提交（若本机已配置则不覆盖）
git config --global user.name >/dev/null 2>&1 || git config --global user.name "2318600486-coder"
git config --global user.email >/dev/null 2>&1 || git config --global user.email "2318600486-coder@users.noreply.github.com"

# 校验令牌 / 用户名
echo "检查 GitHub 令牌..."
USER_JSON="$(curl -sS -H "Authorization: token ${GITHUB_TOKEN}" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/user)"
LOGIN="$(python3 -c 'import json,sys; print(json.load(sys.stdin).get("login",""))' <<<"$USER_JSON")"
if [[ -z "$LOGIN" ]]; then
  echo "GitHub 令牌无效或已过期，请换新 PAT 后重试。"
  echo "接口返回: $USER_JSON"
  exit 1
fi
echo "已登录 GitHub 用户: $LOGIN"
GITHUB_USER="$LOGIN"
REMOTE_CLEAN="https://github.com/${GITHUB_USER}/${REPO_NAME}.git"

# 1. 新建文件夹
step "1 新建文件夹 gitdir"
rm -rf "$WORKDIR"
mkdir -p "$WORKDIR"
cd "$WORKDIR"
pwd

# 2. 初始化（固定用 master，方便后面第 20 步）
step "2 git init"
git init -b master
git status

# 3. 创建 aaa.txt
step "3 创建 aaa.txt"
echo "hello" > aaa.txt
cat aaa.txt

# 4. 查看状态
step "4 git status"
git status

# 5. 加入暂存区
step "5 git add aaa.txt"
git add aaa.txt
git status --short

# 6. 第一次提交
step "6 第一次提交"
git commit -m "第一次提交"
git log --oneline

# 7. 再看状态
step "7 git status"
git status

# 8. 修改文件
step "8 修改 aaa.txt"
echo "world" >> aaa.txt
cat aaa.txt
git status --short

# 9. 撤销修改
step "9 git restore aaa.txt"
git restore aaa.txt
cat aaa.txt
git status --short

# 10. 继续修改
step "10 继续修改 aaa.txt"
echo "java" >> aaa.txt
cat aaa.txt

# 11. 暂存
step "11 git add aaa.txt"
git add aaa.txt
git status --short

# 12. 撤销暂存
step "12 git restore --staged aaa.txt"
git restore --staged aaa.txt
git status --short

# 13. 继续修改
step "13 继续修改 aaa.txt"
echo "git" >> aaa.txt
cat aaa.txt

# 14. 暂存并提交
step "14 git commit -am"
git commit -am "再一次提交"
git log --oneline

# 15. 查看日志
step "15 git log"
git log --oneline --decorate

# 16. 回退上一版本
step "16 git reset HEAD^"
git reset HEAD^
git status --short
git log --oneline --decorate

# 17. 创建分支
step "17 git branch dev"
git branch dev
git branch

# 18. 切换分支
step "18 git switch dev"
git switch dev
git branch

# 19. 在 dev 上修改并提交
step "19 在 dev 修改并提交"
echo "dev branch line" >> aaa.txt
cat aaa.txt
# 若上一步 reset 后仍有未提交改动，一并提交
git add aaa.txt
git commit -m "再一次提交"
git log --oneline --decorate -3

# 20. 切回 master
step "20 git switch master"
git switch master
git branch

# 21. 合并
step "21 git merge dev"
git merge dev -m "merge dev into master"
git log --oneline --decorate -5

# 22. 创建 GitHub 远程库 gitdir2022（已存在则跳过）
step "22 创建 GitHub 仓库 ${REPO_NAME}"
HTTP_CODE="$(curl -sS -o /tmp/gh_create_repo.json -w "%{http_code}" \
  -X POST \
  -H "Authorization: token ${GITHUB_TOKEN}" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/user/repos \
  -d "{\"name\":\"${REPO_NAME}\",\"private\":false,\"auto_init\":false}")"
if [[ "$HTTP_CODE" == "201" ]]; then
  echo "仓库创建成功: ${REMOTE_CLEAN}"
elif [[ "$HTTP_CODE" == "422" ]]; then
  echo "仓库已存在，继续使用: ${REMOTE_CLEAN}"
else
  echo "创建仓库失败 HTTP=${HTTP_CODE}"
  cat /tmp/gh_create_repo.json
  exit 1
fi

# 23. 关联远程并首次推送（令牌只用于本次 push，不写进 origin）
step "23 关联远程并 push -u"
if git remote get-url origin >/dev/null 2>&1; then
  git remote set-url origin "$REMOTE_CLEAN"
else
  git remote add origin "$REMOTE_CLEAN"
fi
git remote -v
# 用临时带令牌的 URL 推送，避免把 PAT 写进 .git/config
git push -u "https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/${GITHUB_USER}/${REPO_NAME}.git" master
# 确保跟踪的是干净的 origin/master
git branch --set-upstream-to=origin/master master 2>/dev/null || true
git remote set-url origin "$REMOTE_CLEAN"
git fetch origin
git status

# 24. 添加 bbb.txt
step "24 创建 bbb.txt"
echo "bbb file content" > bbb.txt
ls -l bbb.txt
git status --short

# 25. 提交 bbb
step "25 提交 bbb.txt"
git add bbb.txt
git commit -m "提交bbb"
git log --oneline --decorate -3

# 26. 再推送
step "26 git push"
git push "https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/${GITHUB_USER}/${REPO_NAME}.git" master
git remote set-url origin "$REMOTE_CLEAN"
git fetch origin
git status
git log --oneline --decorate -5

echo
echo "全部完成！"
echo "本地目录: $WORKDIR"
echo "远程仓库: $REMOTE_CLEAN"
echo
echo "【重要】请立刻去 GitHub 撤销本次 Personal Access Token："
echo "https://github.com/settings/tokens"
echo
