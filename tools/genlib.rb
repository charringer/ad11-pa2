def edges(n)
  (0...n).inject([]){|a,x|a+((x+1)...n).map{|y|[x,y]}}
end
def linewise(l)
  l.map{|t|t.join " "}.join("\n")+"\n"
end
# Example:
# print linewise(edges(6))
