from buildGraph import buildGraph

adjLst = buildGraph()

parentOf = {}
visited = {}
def dfs(currNode, target):
    print(currNode, end=" ")
    visited[currNode] = True
    for child in adjLst[currNode]:
        if child in visited.keys():
            continue
        parentOf[child] = currNode
        if child == target:
            print(target, "$")
            return
        dfs(child, target)

print("\nEnter the start node :  ")
startNode = input()
print("Enter the target node  : ")
targetNode = input()

print("\nThe search path is :")
dfs(startNode, targetNode)
print("The solution path is :")

currNode = targetNode
while(currNode != startNode):
    print(currNode+"<-", end = "")
    currNode = parentOf[currNode]
print(startNode, "$")

