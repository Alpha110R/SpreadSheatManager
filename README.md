# Spread Sheet Manager :memo:


## Capabilities:
1) Creating a new sheet - return the ID of the new sheet.
2) Providing the ability to set a specific cell in a specific sheet with a new value.
   - Value can be a regular expression of its type or a lookup function.
   - The set function will succeed only if:
     - There is no circle.
     - The new value type stands with the column type.
     - Value not null.
3) Get a sheet by ID.

## Assumptions:
- 0 is a valid row index.
- Empty string is a valid value input.
- NULL isn't a valid value input.
- A cell can lookup on a non-existent cell.
- lookup function looks like: "lookup(A,1)" to get recognized.

## Run the project: (if you work with IntelliJ)
1) Run the example in the main and you can add cases.
2) Right-click on src/test/java/SpreadSheetServiceTest -> Run 'SpreadSheetServiceTest' with coverage.

![image](https://github.com/Alpha110R/SpreadSheatManager/assets/68230416/047a8dd8-6ec6-4d51-97ff-b2d94e4b2f7a)



https://github.com/Alpha110R/SpreadSheatManager/assets/68230416/8b8dbb7a-b28c-4d96-9168-0a3311c8ce29



