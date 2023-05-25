from buildGraph import buildGraph
from collections import deque

adjList = buildGraph()

parentOf = {}
visited = {}

def bfs(startNode, target):
    q = deque()
    q.append(startNode)

    while(len(q) > 0):
        sz = len(q)
        while(sz > 0):
            curr = q[0]
            q.popleft()
            print(curr, end=" ")
            visited[curr] = True
            if curr == target:
                print(curr, "$")
                return
            for child in adjList[curr]:
                if child not in visited.keys():
                    parentOf[child] = curr
                    q.append(child)
            sz -= 1

print("\nEnter the start node from which you want to start bfs : ")
startNode = input()
print("Enter the target node which is to be found : ")
targetNode = input()

print("\nThe search path is :")
bfs(startNode, targetNode)
print("The solution path is :")

currNode = targetNode
while(currNode != startNode):
    print(currNode+"<-", end = "")
    currNode = parentOf[currNode]
print(startNode, "$")

