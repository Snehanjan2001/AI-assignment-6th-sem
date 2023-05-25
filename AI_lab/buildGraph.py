def buildGraph():
    adjLst = {}
    print("Enter the Edges\n type 'stop' to stop :")
    vals = ""
    while True:
        vals = input()
        if vals == 'stop':
            break
        vals = vals.split()
        if vals[0] not in adjLst.keys():
            adjLst[vals[0]] = [vals[1]]
        else:
            adjLst[vals[0]].append(vals[1])
        if vals[1] not in adjLst.keys():
            adjLst[vals[1]] = [vals[0]]
        else:
            adjLst[vals[1]].append(vals[0])
    return adjLst