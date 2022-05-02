# Tests
## Test 1: User Sign Up and Log In
Steps:
  1. User launches application and selects "Ok" on the welcome screen.
  2. User selects "Sign Up" and selects "Ok".
  3. User enters desired username in the textbox and selects "Ok". 
  4. User enters desired password in the textbox and selects "Ok".
  5. User selects desired role (Student or Teacher) and selects "Ok".
  6. User selects "Log In" and selects "Ok".
  7. User types their username into the textbox and selects "Ok".
  8. User types their password into the textbox and selects "Ok".

Expected: User now has an account, has logged in and is viewing the homescreen. 

Status: Passed

**Note:** If teacher role was selected, the background is blue. If the student role was selected, the background is orange.

## Test 2: User creates a course. 
Steps: 
  1. User signs in with a **teacher** account (see Test 1 for details). 
  2. User selects the text field next to "New Course", types the desired name, and selects "Create".
  3. User selects "Ok" on the Success window that opens. 
  4. User selects the dropdown menu on the top left, and selects the course they just created.

Expected: Course is created and shows up in dropdown. 

Status: Passed

## Test 3: User creates a quiz. 
Steps: 
  1. User signs in with a **teacher** account (see Test 1 for details).
  2. User selects the desired course in the top left dropdown menu and presses "Select".
  3. User selects the text field next to "New Quiz" and types the desired name of the quiz, then selects "Create".
  4. User selects "Manually" in the dropdown in the window that appears and selects "Ok". 
  5. User selects "Yes" or "No" in the window that appears if they would like the quiz to be randomized or not. 
  6. User types the question in the text field of the window that appears and selects "Ok". 
  7. User types the answer in the text field of the window that appears and selects "Ok". 
  8. If the user would like to add more answer choices, select "Yes" on the window that appears and repeat steps 7-8 as desired. Else, press "No".
  9. If the user would like to add another question, select "Yes" in the window that appears and repeat steps 6-8 as desired. Else, press "No".
  10. User selects the bottom left dropdown and selects the newly created quiz. 

Expected: Quiz is created and shows up in dropdown. 

Status: Passed

## Test 4: User takes quiz
Steps: 
  1. User signs in with a **student** account (see Test 1 for details).
  2. User selects the desired course in the top left dropdown menu and presses "Select".
  3. User selects the desired quiz in the bottom left dropdown menu and presses "Take Quiz". 
  4. User selects "Manually" from the dropdown in the new window and selects "Ok" to take the quiz manually.
  5. User selects the desired answer from the dropdown on the new window that appears, and select "Ok". 
  6. Repeat step 5 for all questions. 

Expected: Quiz is taken and submission stored. 

Status: Passed
