package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showMemberAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_MEMBER;
import static seedu.address.testutil.TypicalMembers.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.member.Member;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteMemberCommand}.
 */
public class DeleteMemberCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Member memberToDelete = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        DeleteMemberCommand deleteMemberCommand = new DeleteMemberCommand(INDEX_FIRST_MEMBER);

        String expectedMessage = String.format(DeleteMemberCommand.MESSAGE_DELETE_MEMBER_SUCCESS,
                Messages.format(memberToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteMember(memberToDelete);

        assertCommandSuccess(deleteMemberCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredMemberList().size() + 1);
        DeleteMemberCommand deleteMemberCommand = new DeleteMemberCommand(outOfBoundIndex);

        assertCommandFailure(deleteMemberCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        Member memberToDelete = model.getFilteredMemberList().get(INDEX_FIRST_MEMBER.getZeroBased());
        DeleteMemberCommand deleteMemberCommand = new DeleteMemberCommand(INDEX_FIRST_MEMBER);

        String expectedMessage = String.format(DeleteMemberCommand.MESSAGE_DELETE_MEMBER_SUCCESS,
                Messages.format(memberToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteMember(memberToDelete);
        showNoMember(expectedModel);

        assertCommandSuccess(deleteMemberCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showMemberAtIndex(model, INDEX_FIRST_MEMBER);

        Index outOfBoundIndex = INDEX_SECOND_MEMBER;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getMemberList().size());

        DeleteMemberCommand deleteMemberCommand = new DeleteMemberCommand(outOfBoundIndex);

        assertCommandFailure(deleteMemberCommand, model, Messages.MESSAGE_INVALID_MEMBER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteMemberCommand deleteMemberFirstCommand = new DeleteMemberCommand(INDEX_FIRST_MEMBER);
        DeleteMemberCommand deleteMemberSecondCommand = new DeleteMemberCommand(INDEX_SECOND_MEMBER);

        // same object -> returns true
        assertTrue(deleteMemberFirstCommand.equals(deleteMemberFirstCommand));

        // same values -> returns true
        DeleteMemberCommand deleteMemberFirstCommandCopy = new DeleteMemberCommand(INDEX_FIRST_MEMBER);
        assertTrue(deleteMemberFirstCommand.equals(deleteMemberFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteMemberFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteMemberFirstCommand.equals(null));

        // different member -> returns false
        assertFalse(deleteMemberFirstCommand.equals(deleteMemberSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteMemberCommand deleteMemberCommand = new DeleteMemberCommand(targetIndex);
        String expected = DeleteMemberCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteMemberCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered member list to show no one.
     */
    private void showNoMember(Model model) {
        model.updateFilteredMemberList(p -> false);

        assertTrue(model.getFilteredMemberList().isEmpty());
    }
}